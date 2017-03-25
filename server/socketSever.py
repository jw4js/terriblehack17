import socketserver
import sys

class TCPHandler(socketserver.BaseRequestHandler):

    def handle(self):
        # connect to the client side
        self.data = self.request.recv(1024).strip()
        print("{} wrote:".format(self.client_address[0]))
        print(self.data)
        self.request.sendall(self.data.upper())

def main():
    HOST, PORT = "localhost", 9999
    with socketserver.TCPServer((HOST,PORT), TCPHandler) as server:
        server.serve_forever()
    return 0

if __name__ == "__main__":
    try:
        sys.exit(main())
    except KeyboardInterrupt:
        sys.exit(1)
