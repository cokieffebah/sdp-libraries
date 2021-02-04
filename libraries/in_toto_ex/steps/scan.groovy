package libraries.in_toto_ex

void call(){
   docker.image(config.inside_image).inside{
      echo ""
   }
   println "printing layout for scan:"
   println get_intoto_layout('scan')
}