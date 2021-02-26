#!/usr/bin/env sh
curl -X POST 'http://localhost:8080/v1/import-csv' -F "file=@input.csv" > result.json
