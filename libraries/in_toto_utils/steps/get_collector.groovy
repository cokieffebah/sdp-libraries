package libraries.in_toto_utils

List call(){
    String queue_name = config.collector?.queue_name ?: "intotoCollector"

    if( null == pipelineConfig[queue_name] ){
        pipelineConfig[queue_name] = []
    }
    
    return pipelineConfig[queue_name]
}

boolean is_collectable( String lib, String step){
    def library = pipelineConfig.libraries[lib]
    return library && library.in_toto && library.in_toto.containsKey(step)
}