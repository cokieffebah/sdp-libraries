package libraries.in_toto_utils

@BeforeStep( { hookContext.library != "in_toto_utils" })
void call(){
    if( get_collector.is_collectable( hookContext.library, hookContext.step) )
        get_collector() << [step: hookContext.step, library: hookContext.library]
}