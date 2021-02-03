package libraries.in_toto_utils

void call(body){
   docker.image(config.inside_image).inside {
       record_start( args.record_config, config)
       body()
       record_stop( args.record_config, config)
   }
}


void record_start(Map args, Map config){

}

void record_stop(Map args, Map config){

}