from gcm import GCM
from db_controller import *



def sendnotification(user,farmname,token,message):
    """
    API key--> Niranjan
    """
    #project server key for gcm

    gcm = GCM('AIzaSyCyoIu2g7f0hyEiKs6P_14k57RuCt-1upo')
    data =   { "message":message}
    response = gcm.json_request(registration_ids=[token], data=data) 
#    print type(response['success'])
    a = response['success'].keys()[0]
#    print a    
    if (token in a):
        AddNotification(user,farmname,token,message)        
        return True
    else:
        return False
