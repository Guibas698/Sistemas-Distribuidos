#!/usr/bin/env bash
set -e
cd "$(dirname "$0")/.."
javac server/*.java admin/*.java user/*.java common/*.java
java -cp . admin.AdminCli
