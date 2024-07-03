You work at AussieBank and your manager asks you to help out on a project. They have a pressing requirement to be able
to process some account transaction files and the programmer who was assigned the task had to go on a very urgent tropical
island holiday. They've started to layout some code but it's far from complete and it's your code now so you can
complete it as you see fit to meet the business requirements. Where the requirements are confusing, vague or
incomplete please make a decision that seems sensible to you and feel free to provide a comment.


Here's the requirements as they are handed over to you.

We have some clients who have requested that they send us files to upload activity into their accounts. They will tell
us what account id the activity relates to, whether it's a Deposit or a Withdraw and the amount. We are not concerned
with where the money came from or where it's going. There are no compliance or double entry book-keeping requirements
we need to concern ourselves with. The clients also enjoy large overdraft facilities so no need to worry about negative
balances.

What we do know is:

- the clients can send us an arbitrary number of files and they can have an arbitrary number of account entries.
Some example files are provided in the resources directory. When multiple files are provided their is a naming
convention to indicate the order.
transaction-log.1.csv
transaction-log.2.csv
transaction-log.3.csv

- when a client uploads a file they are expecting our system to process it as soon as possible and without significant
delay. It's very important to them that files are applied to their accounts in a performant fashion. We can use the
system time of processing the file to indicate the timestamp of this account activity.

- clients can use the bank interface to query their accounts at any time. An account entity can report a balance and a
number of account entries. It's important that an accounts balance and it's entries are consistent. i.e. the balance is
the sum of the applied entries. This is true even while we are in the process of applying the entries from a file.

- each line of account activity must be reported to an internal reporting service. This reporting requirement is not
something which effects our external clients and it is expected that we may return unreported activity to clients.
This is a legacy system which unfortunately suffers from some performance issues. It will hold the calling thread up
for a period of time but supports multiple concurrent clients. This system sits outside of our group and we must use
it as-is. It's important we report all the transactions to the report server as part of the file loading process.


Please provide a solution which meets these requirements. The code you write should be production quality so please
apply any additional aspects (tests etc.) as you see fit.
