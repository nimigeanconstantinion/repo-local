# Deployment Guide

This chart is an umbrella chart for external dependencies used by the testing apps.

## Local-only profile

- Base values: `values.yaml`
- Local overlay: `values-bgd.yaml`

`values-bgd.yaml` is intended for local development only.
It contains relaxed security settings and simple credentials that must not be reused in shared environments.

## Prerequisites

- Kubernetes cluster running locally (for example: Docker Desktop, Rancher Desktop, minikube, kind)
- `helm` and `kubectl` installed and configured for the target cluster/context
- Optional but recommended for URL routing on `/kafka`, `/kibana`, `/keycloak`, `/prometheus`, `/grafana`: ingress-nginx

Install ingress-nginx with the provided helper script:

```bash
./install-ingress-nginx.sh
```

## Validate before deploy

```bash
helm lint .
helm template resurse-testing-app . \
  -f values.yaml \
  -f values-bgd.yaml > /dev/null
```

## Deploy / Upgrade

```bash
NAMESPACE=default
RELEASE=resurse-testing-app

helm upgrade --install "$RELEASE" . \
  --namespace "$NAMESPACE" \
  --create-namespace \
  -f values.yaml \
  -f values-bgd.yaml
```

## Verify deployment

```bash
kubectl -n "$NAMESPACE" get pods
kubectl -n "$NAMESPACE" get svc
kubectl -n "$NAMESPACE" get ingress
```

Useful Helm checks:

```bash
helm -n "$NAMESPACE" list
helm -n "$NAMESPACE" status "$RELEASE"
```

## Useful commands

Inspect pods and rollout state:

```bash
kubectl -n "$NAMESPACE" get pods -o wide
kubectl -n "$NAMESPACE" describe pod <pod-name>
kubectl -n "$NAMESPACE" rollout status statefulset/testing-app-kafka-controller
kubectl -n "$NAMESPACE" rollout status statefulset/keycloak
```

Check recent events and logs:

```bash
kubectl -n "$NAMESPACE" get events --sort-by=.lastTimestamp
kubectl -n "$NAMESPACE" logs deploy/kafka-ui --tail=200
kubectl -n "$NAMESPACE" logs deploy/kibana --tail=200
kubectl -n "$NAMESPACE" logs statefulset/keycloak --tail=200
```

Get generated manifests and Helm history:

```bash
helm -n "$NAMESPACE" get values "$RELEASE" --all
helm -n "$NAMESPACE" get manifest "$RELEASE"
helm -n "$NAMESPACE" history "$RELEASE"
```

Rollback to a previous revision:

```bash
helm -n "$NAMESPACE" rollback "$RELEASE" <revision>
```

Quick local access without ingress (port-forward):

```bash
kubectl -n "$NAMESPACE" port-forward svc/kafka-ui 8080:80
kubectl -n "$NAMESPACE" port-forward svc/kibana 5601:5601
kubectl -n "$NAMESPACE" port-forward svc/keycloak 8081:80
kubectl -n "$NAMESPACE" port-forward svc/prometheus-server 9090:80
kubectl -n "$NAMESPACE" port-forward svc/grafana 3000:80
```

## Local access

When ingress-nginx is installed and `localhost` routes to your local cluster ingress:

- Kafka UI: `http://localhost/kafka`
- Kibana: `http://localhost/kibana`
- Keycloak: `http://localhost/keycloak`
- Prometheus: `http://localhost/prometheus`
- Grafana: `http://localhost/grafana`

Additional NodePort services enabled in local overlay:

- MySQL: `30006`
- Kafka brokers: `30001`, `30002`, `30003`
- Logstash TCP input: `30544`

## Keycloak realm import

Realm data is loaded from `files/keycloak/realm-export.json` via a ConfigMap template:

- Template: `templates/keycloak-realm-configmap.yaml`
- ConfigMap name (default): `app-config`

Update the JSON file and run `helm upgrade --install` again to apply changes.

## Kong local config

For the local profile based on `values-bgd.yaml`, Kong runs in DB-less mode.

- Declarative config file: `files/kong/bgd-kong.yml`
- Generated ConfigMap template: `templates/kong-declarative-configmap.yaml`
- Admin API: `http://localhost/kong-admin`
- API routes through Kong:
  - `http://localhost/kong/consumer/api/messages`
  - `http://localhost/kong/publisher/api/messages`
- Swagger direct on the applications:
  - `http://localhost:8081/publisher/swagger-ui/index.html`
  - `http://localhost:8082/consumer/swagger-ui/index.html`

After changing `files/kong/bgd-kong.yml`, apply the update with:

```bash
helm upgrade --install "$RELEASE" . \
  --namespace "$NAMESPACE" \
  --create-namespace \
  -f values.yaml \
  -f values-bgd.yaml
```

## Uninstall

```bash
helm -n "$NAMESPACE" uninstall "$RELEASE"
```
