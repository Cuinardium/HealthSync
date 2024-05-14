url=http://old-pawserver.it.itba.edu.ar/logs/localhost.$(date +%Y-%m-%d).log
group="paw-2023a-02"
separator="================================================================"

echo -e "getting logs from ${url}, saving to /tmp/${group}.log"
curl ${url} --output /tmp/${group}.log --silent
echo -e "searching for ${group} in /tmp/${group}.log"
grep ${group} -C 30 --color --group-separator=${separator} /tmp/${group}.log
