 docker run --name "xchange-rate-checker" \
    --volume ${PWD}/xchange-rate-checker:/app-data \
    -d rw00/xchange-rate-checker-app:0.0.5
