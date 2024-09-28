SET version=0.0.5

CALL docker build . -t rw00/xchange-rate-checker-app:%version%

CALL docker push rw00/xchange-rate-checker-app:%version%
