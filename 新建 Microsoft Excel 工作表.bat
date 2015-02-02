@echo off
git add -A

set /p val=ÇëÊäÈë°æ±¾±¸×¢

git commit -m '%val%'

echo hust=MC | git push


pause