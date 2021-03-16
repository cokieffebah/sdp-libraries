package libraries.in_toto_ex.steps

String call(String step = null){ 
    String res = step ? "${step}.link" : "in-toto"
    return resource(res) 
}