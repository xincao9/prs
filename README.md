# prs
Personalized Recommendation Service

wrk -t4 -c512 -d30s --script=logminer.lua --latency 'http://localhost:8100/api/logminer/raw/text'