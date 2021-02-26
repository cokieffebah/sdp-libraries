package libraries.in_toto_ex

void call(){
    String registry_url = config.registry_url ?: localhost:5000
    String build_tag = config.build_image_tag ?: "intoto-demo:latest"
    String build_dockerfile = config.build_image_dockerfile ?: "porter/porter.Dockerfile"
  sh(script:"docker build -t ${build_tag} -f ${build_dockerfile} .")
  sh(script:"docker image tag ${build_tag} ${registry_url}/${build_tag}")
}