lifecicle=run

while [[ $# -gt 0 ]]
do
key="$1"

case $key in
    --production)
    lifecicle=run-war
    shift # past argument
    ;;
    --help)    # unknown option
    echo -e "\nUsage: deploy.sh [options]\n"
    echo -e "Options:\n"
    echo -e "--production\t\t\t\t\t\tRuns like production\n"
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

mvn clean
mvn install

cd ./webapp && mvn jetty:${lifecicle}
