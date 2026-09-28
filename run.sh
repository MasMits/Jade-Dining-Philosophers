#!/bin/sh
set -eu

project_dir=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
classes_dir="$project_dir/build/classes"

mkdir -p "$classes_dir"

javac \
  -cp "$project_dir/lib/jade.jar" \
  -d "$classes_dir" \
  "$project_dir/src/main/java/dkai/jade/"*.java

java \
  -cp "$classes_dir:$project_dir/lib/jade.jar" \
  dkai.jade.JadeDiningPhilosophers "$@"
