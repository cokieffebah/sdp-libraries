package libraries.in_toto_ex

void call(){
  sh(script:"docker build -t intoto-demo:latest -f porter/porter.Dockerfile .")
  sh(script:"docker image tag intoto-demo:latest localhost:5000/intoto-demo:latest")
}