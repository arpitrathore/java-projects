# Maven graal vm project generator
THis tool generates a maven project with graal vm maven plugin configured

### Build native binary

```sh
quarkus build --clean --native
```

### Move to $PATH

```sh 
sudo cp ./target/01-mvn-graal-generate-1.0.0-SNAPSHOT-runner /usr/local/bin/mvn-graal-generate
```

### Usage
```sh 
$ mvn-graal-generate group.id artifact.id

# Example
$ mvn-graal-generate com.example my-project
```