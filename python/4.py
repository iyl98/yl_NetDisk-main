from faker import Factory
import requests
import re
import time


def get_page_id(home_url):
    user_agent = Factory.create()
    headers = {"User-Agent": user_agent.user_agent()}
    home = requests.get(url=home_url, headers=headers).text
    home_id = re.findall('<a href="{}/article/details/(.*?)" target="_blank">'.format(home_url), home)
    if len(home_id) == 0:
        get_page_id(home_url)
    page_id = list(set(home_id))
    page_id.sort(key=home_id.index)
    return page_id


def browse_csdn(home_url):
    page_id = get_page_id(home_url)
    while True:
        for i in page_id:
            user_agent = Factory.create()
            headers = {"User-Agent": user_agent.user_agent()}
            page_url = '{}/article/details/{}'.format(home_url, i)
            requests.get(url=page_url,  headers=headers)
            print('{}\tOK'.format(page_url))
            time.sleep(20)


if __name__ == '__main__':
    home_url = 'https://blog.csdn.net/Aurinko324'
    browse_csdn(home_url)