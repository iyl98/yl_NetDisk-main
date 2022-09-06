from selenium import webdriver
from selenium.webdriver.common.keys import Keys
import time

def spider():
   driver = webdriver.Chrome()
   driver.get('http://10.11.1.3')   
   time.sleep(5)
   #input_tag1 = driver.find_element_by_name('ISP_select')  #组合框
   #input_tag1.send_keys('中国联通')    #运营商
   input_tag = driver.find_element_by_xpath("//form[@name='f3']/input[@name = 'DDDDD']")   #通过xpath确定账号矿位置
   input_tag.send_keys("w171101125")  #输入账号
   input_tag2 = driver.find_element_by_xpath("//form[@name='f3']/input[@name = 'upass']")  #通过xpath确定密码框位置
   input_tag2.send_keys("102614")  #输入密码
   input_tag2.send_keys(Keys.ENTER)    #回车
   time.sleep(10)  #10秒后自动关闭浏览器
if __name__ == '__main__':  #运行上面封装的方法
   spider()
