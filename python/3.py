import urllib.request
import requests
import time
import ssl
import random
 
def openUrl(ip, agent):
 headers = {'User-Agent': agent}
 proxies = {'http' : ip}
 ######################################################################################################################
 requests.get("https://aurinko.blog.csdn.net/article/details/119939295?spm=1001.2014.3001.5502", headers=headers, proxies=proxies, verify=True)
 requests.get("https://aurinko.blog.csdn.net/article/details/120422338?spm=1001.2014.3001.5502", headers=headers, proxies=proxies, verify=True)
 requests.get("https://aurinko.blog.csdn.net/article/details/124670907?spm=1001.2014.3001.5502", headers=headers, proxies=proxies, verify=True)
 requests.get("https://aurinko.blog.csdn.net/article/details/1239286992", headers=headers, proxies=proxies, verify=True)
 requests.get("https://aurinko.blog.csdn.net/article/details/124525252", headers=headers, proxies=proxies, verify=True)
 requests.get("https://download.csdn.net/download/Aurinko324/85343583?spm=1001.2014.3001.5503", headers=headers, proxies=proxies, verify=True)
########################################################################################################################
 ssl._create_default_https_context = ssl._create_unverified_context
 print("Access to success.")
 

def randomIP():
 ip = random.choice(['120.78.78.141', '122.72.18.35', '120.92.119.229', '183.64.239.19', '123.145.135.38', '183.64.239.19', '123.57.246.163', '27.42.168.46', '123.57.246.163', '175.24.112.3', '222.223.182.66', '121.22.90.82', '222.223.182.66', '106.107.136.217', '120.71.147.222'])
 return ip
 
#User-Agent
#User-Agent来源：http://www.useragentstring.com/pages/useragentstring.php
def randomUserAgent():
 UserAgent = random.choice(['Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.84 Safari/537.36',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:101.0) Gecko/20100101 Firefox/101.0',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_3) AppleWebKit/537.75.14 (KHTML, like Gecko) Version/7.0.3 Safari/7046A194A'])
 return UserAgent
 
if __name__ == '__main__':
  for i in range(99999):
      ip = randomIP()
      agent = randomUserAgent()
      openUrl(ip, agent)
      time.sleep(13)
      
