Feature: Amazon Add to Cart Feature
	
@MainTest
Scenario Outline: Amazon flow

Given user is signed in on Amazon
When user adds "<quantity>" items to the cart
Then user should be able to see "<quantity>" items in the cart
And user should be able to place the order


Examples:
	| quantity |
	| 2 	   |

