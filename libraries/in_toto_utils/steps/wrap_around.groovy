package libraries.in_toto_utils

void call(Map args = [:], body = {}){
    Map start_args = args.start ?: [:]
    Map stop_args = args.stop ?: [:]

    docker.image(config.inside_image).inside {
        unstash 'workspace'
        record_start( start_args, config)
        body()
        record_stop( stop_args, config)
        stash 'workspace'
    }
}


void record_start(Map args, Map config){
    
}

void record_stop(Map args, Map config){

}