#!/usr/bin/env python2
import requests
import json
import sys
import socketserver

API_KEY = "851491bcca225c2e3dde:7787437fafe08a29651ef8946b772839a08785dd"

def list_by_color(hex_color):
	raw = requests.get(url="https://" + API_KEY + "@api.shutterstock.com/v2/images/search?query=food&per_page=256&color=#" + hex_color + "&category=Food+and+Drink&safe=true").json()["data"]
	image_list = set()
	for i in raw:
		image_list.add(i["assets"]["preview"]["url"])
	return image_list

def get_best_by_colors(colors):
	s = list_by_color(colors[0])
	for i in range(1,6):
		s &= list_by_color(colors[i])
	return s.pop()


class TCPHandler(socketserver.BaseRequestHandler):
    def handle(self):
        # connect to the client side
        data = str(self.request.recv(25)).strip()
        print("{} wrote: {}".format(self.client_address[0],data))
        colors = data.split(' ')[1:]
        self.request.sendall(get_best_by_colors(colors).encode("UTF_8"))

def main():
	HOST, PORT = "localhost", 9999
	with socketserver.TCPServer((HOST,PORT), TCPHandler) as server:
		server.serve_forever()
	return 0

#print(data.json()["data"][0]["assets"]["large_thumb"])
# /v2/images/licenses/{id}/downloads
try:
	sys.exit(main())
except KeyboardInterrupt:
	sys.exit(1)