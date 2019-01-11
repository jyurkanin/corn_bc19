all:
	bc19compile -d jsrc -f -o bin/compiled_bot.js
run:
	bc19run --rc bin/compiled_bot.js --bc bin/compiled_bot.js
