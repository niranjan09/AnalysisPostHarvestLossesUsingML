from sklearn import tree
from pymongo import MongoClient


def predict_disease(current_temp,current_hum):

	clf=tree.DecisionTreeClassifier()

	client=MongoClient()
	db=client.server_db
	cursor=db.corpusdisease.find()

	x=[]
	y=[]
	for docs in cursor:
		 item=[]
		 item.append(docs["Temperature"])
		 item.append(docs["Humidity"])
		 x.append(item)
		 y.append(docs["Diseaseno"])
	 
	clf.fit(x,y)

	z = []
	w = []


	#z.append(int(raw_input("Enter temp: ")))
	#z.append(int(raw_input("Enter Humidity: ")))
	z=[int(current_temp),int(current_hum)]

	w.append(z)

	a = clf.predict(w)
	
	print "End of Processing ..."
	if a[0] == 1:
		return "Predicted disease is "+ "Onion Smut" 
	if a[0] == 2:
		return "Predicted disease is "+ "Seedling Disorder"
	if a[0] == 3:
		return "Predicted disease is "+ "Botrytis Leaf Blight"
	if a[0] == 4:
		return "Predicted disease is "+ "Downy mildew"
	if a[0] == 5:
		return "Predicted disease is "+ "Purple blotch"
	if a[0] == 6:
		return "Predicted disease is "+ "Fusarium Basal Rot"
	else:
		return "No disease"
