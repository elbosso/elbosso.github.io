#!/usr/bin/env python3
from influxdb import InfluxDBClient
import requests
import json
import datetime
import socket

#host of the influxdb used to store the data
influxhost=''
#port of the influxdb used to store the data
influxport=
#username for accessing the influxdb used to store the data
influxusername=''
#password for accessing the influxdb used to store the data
influxpassword=''
#name of the influxdb database used to store the data
monitoringdbname=''
#user agent (python requests standard user agent is blocked by the api
useragent='Mozilla/5.0 (X11; Linux x86_64; rv:10.0) Gecko/20100101 Firefox/10.0'
#latitude of the location (german: Breitengrad)
latitude=
#longitude of the location (german: Längengrad)
longitude=
#radius for the search in km
radius=3.5
#api-key
token=''
#API url
url='https://creativecommons.tankerkoenig.de/json/list.php?lat={latitude}&lng={longitude}&rad={radius}&sort=dist&type=all&apikey={token}'.format(latitude=latitude,longitude=longitude,radius=radius,token=token)
#name of the location
locality=""
#value for the host tag when writing to the influxdb
monitoringhost=socket.getfqdn()
#if you want the ip address rather than the hosts name, do
#monitoringhost=socket.gethostbyname(socket.getfqdn())

client = InfluxDBClient(host=influxhost, port=influxport)
#if influx requires authentication, use the following line instead of the one above:
#client = InfluxDBClient(host=influxhost, port=influxport,username=influxusername, password=influxpassword)

databases = client.get_list_database()


databaseAlreadyThere =False


for item in databases:
    if item['name'] == monitoringdbname:
        databaseAlreadyThere = True


if databaseAlreadyThere == False:
    client.create_database(monitoringdbname)


client.switch_database(monitoringdbname)


headers = {
    'User-Agent': useragent
}

response = requests.get(url, headers=headers)
data = json.loads(response.content.decode('utf-8'))
if data['ok']:
    stations=data['stations']
    for station in stations:
        print(station['name'])
        lon=station['lng']
        lat=station['lat']
        stats={}
        stats["Name"]=station['name']
        stats["Longitude"]=lon
        stats["Latitude"]=lat
        stats["Distance"]=station['dist']
        if station['e5'] is not None:
            stats["E5"]=station['e5']
        if station['e10'] is not None:
            stats["E10"]=station['e10']
        if station['diesel'] is not None:
            stats["Diesel"]=station['diesel']
        json_body = []
        jb={}
        jb["measurement"]="TankerKoenig"
        tags={}
        tags["Name"]=station['name']
        tags["Server"]=monitoringhost
        tags["Location"]=station['place']
        tags["Address"]=station['street']
        jb["tags"]=tags
        jb["fields"]=stats
        json_body.append(jb)
        client.write_points(json_body)
