### 1. Build native binary

```sh
quarkus build --clean --native
```

### 2. Move to $PATH

```sh 
sudo cp ./target/01-mvn-graal-generate-1.0.0-SNAPSHOT-runner /usr/local/bin/mvn-graal-generate
```