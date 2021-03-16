package libraries.in_toto_ex

void call(){
   sh( "tar --exclude '.git' -zcvf demo-project.tar.gz demo-project scan.log")
}