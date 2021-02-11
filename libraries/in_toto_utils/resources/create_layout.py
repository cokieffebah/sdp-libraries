from securesystemslib import interface
from in_toto.models.layout import Layout
from in_toto.models.metadata import Metablock
import json
import argparse

def main():
  parser = argparse.ArgumentParser()
  parser.add_argument("-f", "--funcpath", type=str, default=None, 
      help="the path to functionary key")

  parser.add_argument("signerpath", type=str,
   help="the path to the signer key")    

  args = parser.parse_args()    
  signer_path = args.signerpath
  func_path = signer_path + ".pub"

  if args.funcpath:
    func_path = args.funcpath

  with open('layout.json') as f:
    read_data = f.read()
    print('in-toto.json: ' + read_data)
    config_json = json.loads(read_data)
    create_layout(config_json, signer_path, func_path)
    print('create_layout.main')


def create_layout(config_json, signer_path, func_path): 
  config_json["keys"] = {} 
  for key_path in config_json["key_paths"]:
    key_data = interface.import_rsa_publickey_from_file(key_path)
    config_json["keys"][key_data["keyid"]]= key_data

  layout = Layout.read(config_json)  
  metadata = Metablock(signed=layout)

  signer_key = interface.import_rsa_privatekey_from_file(signer_path)
  metadata.sign(signer_key)
  metadata.dump("the.layout")
  print('created the.layout')


def create_rootlayout(config_json):  
  # Load Alice's private key to later sign the layout
  key_alice = interface.import_rsa_privatekey_from_file("alice")
  # Fetch and load Bob's and Carl's public keys
  # to specify that they are authorized to perform certain step in the layout
  key_bob = interface.import_rsa_publickey_from_file("../functionary_bob/bob.pub")
  key_carl = interface.import_rsa_publickey_from_file("../functionary_carl/carl.pub")
  
  layout = Layout.read({
      "_type": "layout",
      "keys": {
          key_bob["keyid"]: key_bob,
          key_carl["keyid"]: key_carl,
      },
      "steps": [{ 
        "name": "fetch-upstream",
        "threshold": 1,
        "expected_materials": [],
        "expected_products": [["CREATE", "demo-project/vcs.log"]],
        "pubkeys": [key_bob["keyid"]],
        "expected_command": [],
      },{
          "name": "update-version",
          "expected_materials": [["MATCH", "demo-project/*", "WITH", "PRODUCTS",
                                "FROM", "fetch-upstream"], ["DISALLOW", "*"]],
          "expected_products": [["ALLOW", "demo-project/foo.py"], ["DISALLOW", "*"]],
          "pubkeys": [key_bob["keyid"]],
          "expected_command": [],
          "threshold": 1,
        },{
          "name": "package",
          "expected_materials": [
            ["MATCH", "demo-project/*", "WITH", "PRODUCTS", "FROM",
             "update-version"], ["MATCH", "demo-project/*", "WITH", "PRODUCTS", "FROM",
             "fetch-upstream"], ["DISALLOW", "*"],
          ],
          "expected_products": [
              ["CREATE", "demo-project.tar.gz"], ["DISALLOW", "*"],
          ],
          "pubkeys": [key_carl["keyid"]],
          "expected_command": [
              "tar",
              "--exclude",
              ".git",
              "-zcvf",
              "demo-project.tar.gz",
              "demo-project",
          ],
          "threshold": 1,
        }],
      "inspect": [{
          "name": "untar",
          "expected_materials": [
              ["MATCH", "demo-project.tar.gz", "WITH", "PRODUCTS", "FROM", "package"],
              # FIXME: If the routine running inspections would gather the
              # materials/products to record from the rules we wouldn't have to
              # ALLOW other files that we aren't interested in.
              ["ALLOW", ".keep"],
              ["ALLOW", "alice.pub"],
              ["ALLOW", "root.layout"],
              ["DISALLOW", "*"]
          ],
          "expected_products": [
              ["MATCH", "demo-project/vcs.log", "WITH", "PRODUCTS", "FROM", "fetch-upstream"],
              ["MATCH", "demo-project/foo.py", "WITH", "PRODUCTS", "FROM", "update-version"],
              # FIXME: See expected_materials above
              ["ALLOW", "demo-project/.git/*"],
              ["ALLOW", "demo-project.tar.gz"],
              ["ALLOW", ".keep"],
              ["ALLOW", "alice.pub"],
              ["ALLOW", "root.layout"],
              ["DISALLOW", "*"]
          ],
          "run": [
              "tar",
              "xzf",
              "demo-project.tar.gz",
          ]
        }],
  })

  metadata = Metablock(signed=layout)
  metadata.sign(key_alice)
  metadata.dump("root.layout")
  print('created root.layout')


def create_sublayout(config_json):
  key_bob = interface.import_rsa_publickey_from_file("../functionary_bob/bob.pub")
  key_bob_private = interface.import_rsa_privatekey_from_file("../functionary_bob/bob")

  upstream_layout = Layout.read({ 
    "_type" : "layout",
    "keys": {
          key_bob["keyid"]: key_bob,
    },
    "steps" : [
      {
          "name": "clone",
          "expected_materials": [],
          "expected_products": config_json["named"]["clone"]["expected_products"],
          "pubkeys": [key_bob["keyid"]],
          "expected_command": [
              "git",
              "clone",
              "https://github.com/in-toto/demo-project.git"
          ],
          "threshold": 1,
      },{
        "name": "vcs-log",
        "threshold": 1,
        "expected_materials": [["MATCH", "demo-project/*", "WITH", "PRODUCTS", "FROM", "clone"]],
        "expected_products": config_json["named"]["vcs-log"]["expected_products"],
        "pubkeys": [
          key_bob["keyid"]
        ],
        "expected_command": [
          "git", 
          "log",  
          "--pretty=oneline", 
          '>', 
          'demo-project/vcs.log'
          ]
      }
     ],
     "inspect": [],
  })
  
  upstream_metadata = Metablock(signed=upstream_layout)

  # Sign and dump layout to "sub layout"
  upstream_metadata.sign(key_bob_private)
  # figure out how to get alices pub key and make her part of the link
  upstream_metadata.dump("fetch-upstream.776a00e2.link")
  print('created sub layout')

if __name__ == '__main__':
  main()
