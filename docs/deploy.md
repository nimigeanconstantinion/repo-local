# Deployment Notes

## Environments

- Base config: `values.yaml`
- Environment overlays:
  - `values-dev.yaml`
  - `values-staging.yaml`
  - `values-prod.yaml`
  - `values.local.yaml` for local cluster/dev machine

## Recommended command

```bash
helm upgrade --install testing-app-deps . -n <namespace> \
  -f values.yaml \
  -f values-<env>.yaml
```

## Verification

```bash
helm lint .
helm template testing-app-deps . -f values.yaml -f values-<env>.yaml
```
