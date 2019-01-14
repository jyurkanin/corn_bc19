all:
	bc19compile -d jsrc -f -o bin/compiled_bot.js
run:
	bc19run -d  -s 17 --rc bin/compiled_bot.js --bc bin/compiled_bot.js
