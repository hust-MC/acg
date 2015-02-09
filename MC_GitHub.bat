@echo off

git add -A

set /p val=\nÇëÊäÈë°æ±¾±¸×¢\n

git commit -m '%val%'

git push

pause
