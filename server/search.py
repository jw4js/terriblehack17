import requests
import json

client_id="851491bcca225c2e3dde"
client_secret="7787437fafe08a29651ef8946b772839a08785dd"
data = requests.get(url="https://${client_id}:${client_secret}@api.shutterstock.com/v2/images/search?query=food")
print(data.json())
