# Start the application locally
* install netcat on laptop
* install kafka on laptop
* Run kakfa before starting application.
* Create a topic called "sensor-data-group"
* RUN the modules main services.

Run below commands in terminal for testing purpose
* echo "sensor_id=t1; value=36" | nc -u -w1 127.0.0.1 3355
* echo "sensor_id=h1; value=20%" | nc -u -w1 127.0.0.1 3344

