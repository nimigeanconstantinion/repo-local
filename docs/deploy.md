# Deployment Notes

## Environments

- Base config: `values.yaml`
- Environment overlays:
  - `values-dev.yaml`
  - `values-staging.yaml`
  - `values-prod.yaml`
  - `values-bgd.yaml` for local cluster/dev machine

## Recommended command

```bash
helm upgrade --install resurse-testing-app . -n <namespace> \
  -f values.yaml \
  -f values-<env>.yaml
```

## Verification

```bash
helm lint .
helm template resurse-testing-app . -f values.yaml -f values-<env>.yaml
```
