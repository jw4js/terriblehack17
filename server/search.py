#!/usr/bin/env python2
import requests
import json

data = requests.get(url="https://851491bcca225c2e3dde:7787437fafe08a29651ef8946b772839a08785dd@api.shutterstock.com/v2/images/search?query=food")
print(data.json())
