import requests
import time
from datetime import date
current_date = date.today()

url = 'http://pawserver.it.itba.edu.ar/logs/paw-2023a-02.warnings.' + str(current_date) + '.log'
last_position = 0

while True:
    response = requests.get(url)
    content = response.text

    if len(content) > last_position:
        new_data = content[last_position:].removesuffix('\n');
        print(new_data)
        last_position = len(content)

    time.sleep(0.5)
