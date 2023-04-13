skip_test=''

# if --skip_test then -> define skip_test
while [[ $# -gt 0 ]]
do
key="$1"

case $key in
    --skip_test)
    skip_test='-DskipTests'
    shift # past argument
    ;;
    --help)    # unknown option
    echo -e "\nUsage: deploy.sh [options]\n"
    echo -e "Options:\n"
    echo -e "--skip_test\t\t\t\t\t\tSkips tests when building the project.\n"
    echo -e "--help\t\t\t\t\t\t\tShows this help message.\n"
    exit 0
    ;;
    *)
    echo -e "\n[ERROR] Invalid option: $1"
    echo -e "Use --help to see a list of valid options.\n"
    exit 1
    ;;
esac
done

echo -e "\n\nRunning deploy.sh\n\n"

mvn clean
if [ $? -ne 0 ]; then
    echo -e "\n[ERROR] mvn clean failed!"
    exit 1
fi

mvn install ${skip_test}
if [ $? -ne 0 ]; then
    echo -e "\n[ERROR] mvn install failed!"
    exit 1
fi

# if ./webapp/target/webapp.war does not exist, then exit
if [ ! -f ./webapp/target/webapp.war ]; then
    echo -e "\n[ERROR] webapp/target/webapp.war not found!"
    exit 1
fi

# if ENV variable PAMPER_USER is not set, then exit
if [ -z "$PAMPERO_USER" ]; then
    echo -e "\n[ERROR] ENV var PAMPERO_USER is not set!"
    exit 1
fi

# change if needed
local_port=2222
# in seconds -> change if needed
ssh_tunnel_uptime=10
# change if needed
paw_user="paw-2023a-02"

# create temporary ssh-tunnel
ssh -f -L ${local_port}:10.16.1.110:22 ${PAMPERO_USER}@pampero.itba.edu.ar sleep ${ssh_tunnel_uptime}

# upload ./webapp/target/webapp.war to web/app.war on server
sftp -P ${local_port} ${paw_user}@localhost <<< $"put ./webapp/target/webapp.war web/app.war"
