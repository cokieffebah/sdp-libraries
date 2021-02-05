package libraries.in_toto_utils

@BeforeStep( { hookContext.library != "in_toto_utils" })
void call(){
    if( is_collectable( hookContext.library, hookContext.step) )
        get_collector() << [step: hookContext.step, library: hookContext.library]
}

boolean is_collectable( String lib, String step){
    def library = pipelineConfig.libraries[lib]
    return library && library.in_toto && library.in_toto.containsKey(step)
}