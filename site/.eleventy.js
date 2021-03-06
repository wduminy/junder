// 11ty configuration
const
	dev = global.dev = (process.env.ELEVENTY_ENV === 'development'),
	now = new Date();

module.exports = config => {

	/* --- PLUGINS --- */

	// navigation
	config.addPlugin(require('@11ty/eleventy-navigation'));


	/* --- TRANSFORMS -- */

	// inline assets
	config.addTransform('inline', require('./lib/transforms/inline'));

	// minify HTML
	config.addTransform('htmlminify', require('./lib/transforms/htmlminify'));

	// CSS processing
	config.addTransform('postcss', require('./lib/transforms/postcss'));


	/* --- FILTERS --- */

	// format dates
	const dateformat = require('./lib/filters/dateformat');
	config.addFilter('datefriendly', dateformat.friendly);
	config.addFilter('dateymd', dateformat.ymd);

	// format word count and reading time
	config.addFilter('readtime', require('./lib/filters/readtime'));


	/* --- SHORTCODES --- */

	// page navigation
	config.addShortcode('navlist', require('./lib/shortcodes/navlist.js'));

	/* --- WATCH FOLDERS --- */

	config.addWatchTarget('./src/scss/');
	config.addWatchTarget('./src/js/');
	config.addPassthroughCopy("svg");
	config.addPassthroughCopy("src/images");
	// 11ty defaults
	return {

		dir: {
			input: 'src',
			output: '_site'
		}

	};
};
