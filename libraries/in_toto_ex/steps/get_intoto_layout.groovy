String call(String step = null){ 
    String res = step ? "${step}.link" : "in-toto"
    return resource(res) 
}