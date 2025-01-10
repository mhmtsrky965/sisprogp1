import matplotlib.pyplot as plt
import socket
import threading
import time
from hasup_pb2 import Capacity

class CapacityPlotter:
    def __init__(self):
        self.fig, self.ax = plt.subplots()
        self.server_data = {
            1: {'times': [], 'capacities': [], 'color': 'blue'},
            2: {'times': [], 'capacities': [], 'color': 'red'},
            3: {'times': [], 'capacities': [], 'color': 'green'}
        }
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.socket.bind(('localhost', 8000))
        self.socket.listen(1)
        
    def start(self):
        threading.Thread(target=self.listen_for_data).start()
        self.update_plot()
        plt.show()
        
    def listen_for_data(self):
        while True:
            conn, addr = self.socket.accept()
            data = conn.recv(1024)
            capacity = Capacity.FromString(data)
            self.update_data(capacity)
            
    def update_data(self, capacity):
        server_id = capacity.server_id
        self.server_data[server_id]['times'].append(time.time())
        self.server_data[server_id]['capacities'].append(capacity.server_status)
        
    def update_plot(self):
        while True:
            self.ax.clear()
            for server_id, data in self.server_data.items():
                self.ax.plot(data['times'], data['capacities'], 
                           color=data['color'], 
                           label=f'Server {server_id}')
            self.ax.legend()
            self.ax.set_title('Server Capacities Over Time')
            self.ax.set_xlabel('Time')
            self.ax.set_ylabel('Number of Subscribers')
            plt.pause(5)

if __name__ == '__main__':
    plotter = CapacityPlotter()
    plotter.start()
