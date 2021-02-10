package libraries.in_toto_utils

@BeforeStep( { hookContext.library != "in_toto_utils" })
void call(){
    if( get_collector.can_collect( hookContext.library, hookContext.step) )
        get_collector.get_collector() << [step: hookContext.step, library: hookContext.library]
}