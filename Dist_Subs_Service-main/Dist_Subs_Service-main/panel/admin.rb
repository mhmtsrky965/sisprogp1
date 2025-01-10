require 'socket'
require_relative 'hasup_pb'

class AdminClient
  def initialize
    @servers = [
      {port: 7001, socket: nil},
      {port: 7002, socket: nil},
      {port: 7003, socket: nil}
    ]
    @fault_tolerance = read_config
    connect_to_servers
  end
  
  def read_config
    File.read('dist_subs.conf').match(/fault_tolerance_level = (\d+)/)[1].to_i
  end
  
  def connect_to_servers
    @servers.each do |server|
      server[:socket] = TCPSocket.new('localhost', server[:port])
    end
  end
  
  def start_servers
    config = Configuration.new(
      fault_tolerance_level: @fault_tolerance,
      method: DemandType::STRT
    )
    
    @servers.each do |server|
      server[:socket].write(config.serialize)
      response = Message.parse(server[:socket].read)
      monitor_capacity(server) if response.response == ResponseType::YEP
    end
  end
  
  def monitor_capacity
    Thread.new do
      while true
        check_server_capacities
        sleep 5
      end
    end
  end
  
  private
  
  def check_server_capacities
    @servers.each do |server|
      capacity_request = Message.new(demand: DemandType::CPCTY)
      server[:socket].write(capacity_request.serialize)
      capacity = Capacity.parse(server[:socket].read)
      send_to_plotter(capacity)
    end
  end
end

AdminClient.new.start_servers
