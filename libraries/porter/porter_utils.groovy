void call(){}

void image_wrap(body){
    String workspace = config.workspace ?: 'workspace'
    docker.image(config.inside_image).inside {
        unstash workspace
        body()
        stash workspace
    }
}