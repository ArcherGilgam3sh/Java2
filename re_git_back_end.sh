#!/bin/bash

rm -rf back_end
mkdir back_end
cp -a ../JAVA2-Project/. back_end/
rm -r back_end/.idea

commit_message=""
if [ -z "$1" ]; then
    commit_message=$(date +"%Y-%m-%d %H:%M:%S")
else
    commit_message=$1
fi

git add .
git commit -m "$commit_message"
git push