package libraries.in_toto_utils

List call(){
    return get_collector()
}

List get_collector(){
    String queue_name = config.collector?.queue_name ?: "intotoCollector"

    if( null == pipelineConfig[queue_name] ){
        pipelineConfig[queue_name] = []
    }
    
    return pipelineConfig[queue_name]
}

boolean can_collect( String lib, String step){
    def library = pipelineConfig.libraries[lib]
    return library && library.in_toto && library.in_toto.containsKey(step)
}

Map step_config( String lib, String step ){
    def ret = null
    def library = pipelineConfig.libraries[lib]
    if( can_collect(lib,step) ){
        ret = library.in_toto[step]
    } 

    return ret
}

Map record_config( String lib, String step ){
    return step_config( lib, step )?.record 
}

Map step_layout_config( String lib, String step ){
    return step_config( lib, step )?.layout 
}

Map layout_config(){
    return config.layout ?: [:]
}

Map inspect_config(){
    return config.layout?.inspect ?: [:] 
}

void intoto_wrap(body){
    String workspace = config.workspace ?: 'workspace'
    docker.image(config.inside_image).inside(config.inside?.args ) {
        unstash workspace
        body()
        stash workspace
    }
}

void generate_functionary_keys(){
    String functionary_path = config.functionary.path
    def ls_status = sh(returnStatus:true, script:"ls ${functionary_path}")

    if( 0 == ls_status ){}
    else if( config.functionary.generate ){
        sh("in-toto-keygen -t rsa -b 2048 ${functionary_path}")
        archiveArtifacts( artifacts: "${functionary_path}.pub" )
    } else {
        write_functionary_keys(functionary_path)
    }
}

void write_functionary_keys(String functionary_path = null){
    functionary_path = functionary_path ?: config.functionary.path
    if( config.functionary.private_cred ){
        withCredentials([file(credentialsId: config.functionary.private_cred, variable: 'privateKey')]) {
            writeFile( file:functionary_path, text:privateKey )
        }
    }

    if( config.functionary.public_cred ){
        withCredentials([file(credentialsId: config.functionary.public_cred, variable: 'publicKeyFile')]) {     
            sh( "cp -p ${publicKeyFile} ${functionary_path}.pub")
            archiveArtifacts( artifacts: "${functionary_path}.pub" )
        }
    }
}