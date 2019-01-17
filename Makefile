blue:
	bc19compile -d jsrc -f -o bin/blue/compiled_bot.js
red:
	bc19compile -d jsrc -f -o bin/red/compiled_bot.js
run:
	bc19run -d  -s 17 --rc bin/red/compiled_bot.js --bc bin/blue/compiled_bot.js
