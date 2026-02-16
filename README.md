# testing-app-deps

Umbrella Helm chart for external dependencies (MySQL, Kafka, ELK, Keycloak).

## Project layout

- `Chart.yaml`, `Chart.lock`: chart metadata and dependency lock.
- `charts/`: vendored dependency charts (`.tgz`).
- `templates/`: local templates owned by this chart.
- `files/`: static files consumed by templates (for example Keycloak realm export).
- `values.yaml`: base/default values.
- `values.local.yaml`: local machine overrides.
- `values-dev.yaml`, `values-staging.yaml`, `values-prod.yaml`, `values-bgd.yaml`: environment overlays.
- `RUN-LOCAL.txt`: local run/deploy commands.
- `RUN-ENV.txt`: environment run/deploy commands.
- `docs/`: architecture and deployment notes.

## Deploy

```bash
helm dependency update
helm upgrade --install testing-app-deps . -n <namespace> \
  -f values.yaml \
  -f values-<env>.yaml
```

Local example:

```bash
helm upgrade --install testing-app-deps . -n <namespace> \
  -f values.yaml \
  -f values.local.yaml
```

## Keycloak realm import

- Realm file source: `files/keycloak/realm-export.json`
- ConfigMap template: `templates/keycloak-realm-configmap.yaml`
- Keycloak mount/import is configured in `values.yaml` via `extraVolumes`, `extraVolumeMounts`, and `KEYCLOAK_EXTRA_ARGS=--import-realm`.
