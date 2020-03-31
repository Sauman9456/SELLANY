const functions = require('firebase-functions');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
const request = require('request-promise');

exports.indexPostsToElastic = functions.database.ref('/posts/{post_id}')
	.onWrite((change, context) => {
		let post_id = context.params.post_id; // get post_id value from params

		let postData = change.after.val();

		
		console.log('Indexing post:', postData);
		
		let elasticSearchConfig = functions.config().elasticsearch;
		let elasticSearchUrl = elasticSearchConfig.url + 'posts/_doc/post' + post_id;
		let elasticSearchMethod = postData ? 'POST' : 'DELETE';
		
		return request({
			method: elasticSearchMethod,
			url: elasticSearchUrl,
			auth:{
				username: elasticSearchConfig.username,
				password: elasticSearchConfig.password,
			},
    body: postData,
	json: true
  });
});