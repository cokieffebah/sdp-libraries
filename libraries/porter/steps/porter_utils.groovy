package libraries.porter_utils

void call(){}

void image_wrap(body){
    String workspace = config.workspace ?: 'workspace'
    docker.image(config.inside_image).inside {
        unstash workspace
        body()
        stash workspace
    }
}

void image_wrap_record(String step, body){
    String workspace = config.workspace ?: 'workspace'
    docker.image(config.inside_image).inside(config.inside?.args) {
        unstash workspace
        intoto_utils.generate_functionary_keys()
        intoto_record.record_start( step )
        body()
        intoto_record.record_stop( step )
        stash workspace
    }
}