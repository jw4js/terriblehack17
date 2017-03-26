#!/usr/bin/env python2
import requests
import json
import sys
import socketserver
import random

API_KEY = "851491bcca225c2e3dde:7787437fafe08a29651ef8946b772839a08785dd"

def list_by_color(hex_color):
	raw = requests.get(url="https://" + API_KEY + "@api.shutterstock.com/v2/images/search?query=food&per_page=256&color=#" + hex_color + "&category=Food+and+Drink&safe=true").json()["data"]
	image_list = set()
	for i in raw:
		image_list.add(i["assets"]["preview"]["url"])
	return image_list

def get_best_by_colors(colors):
	s = list_by_color(colors[0])
	o = s
	for i in range(1,len(colors)):
		s &= list_by_color(colors[i])
		if len(s) < 40:
			s = o
			break
		o = s
	s = list(s)
	return s[random.randint(0,len(s))]


class TCPHandler(socketserver.BaseRequestHandler):
    def handle(self):
        # connect to the client side
        data = str(self.request.recv(1024)).strip()
        colors = data.split(' ')[1:]
        response = get_best_by_colors(colors).encode("UTF_8")
        self.request.sendall(response)
        print("{} rx {} tx {}".format(self.client_address[0],data,response))

def main():
	HOST, PORT = "10.84.66.119", 9999
	with socketserver.TCPServer((HOST,PORT), TCPHandler) as server:
		server.serve_forever()
	return 0

#print(data.json()["data"][0]["assets"]["large_thumb"])
# /v2/images/licenses/{id}/downloads
try:
	sys.exit(main())
except KeyboardInterrupt:
	sys.exit(1)