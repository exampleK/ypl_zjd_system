
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
 
import tornado.ioloop
from tornado.web import RequestHandler,StaticFileHandler
import pymysql,os,json
from pymysql import cursors
import xlrd
import sys
import datetime,time

list_temp_sql_result = []
list_temp_sql_result2 = []

class SelectHandler(tornado.web.RequestHandler):
    def set_default_headers(self):
        self.set_header("Access-Control-Allow-Origin", "*") # 这个地方可以写域名
        self.set_header("Access-Control-Allow-Headers", "x-requested-with")
        self.set_header('Access-Control-Allow-Methods', 'POST, GET, OPTIONS')
    def by_name(self,t):
        return(t[0])
    def calu_oil_L(self,oil_H):
        import math
        oil_R = 1.2
        oil_L = 6.05
        oil_C = 0.6
        L = (oil_L*((math.pi * oil_R**2)/2-(oil_R-oil_H)*math.sqrt(2*oil_H*oil_R-oil_H**2)-oil_R**2*math.asin(1-oil_H/oil_R))+(math.pi*oil_C)/(3*oil_R)*(3*oil_R**2*oil_H-oil_R**3+(oil_R-oil_H)**3))
        return L
    def get(self):
        global list_temp_sql_result,list_temp_sql_result2
        list_temp_sql_result = []
        list_temp_sql_result2 = []
        
        # 查数据
        conn = pymysql.connect(host='127.0.0.1', port=3306, user='root', passwd='root', db='test')
        cursor = conn.cursor()

        temp1 = "select * from boom_car"
        cursor.execute(temp1)
        temp_sql_result1 = cursor.fetchall()
        boom_date = ((max(temp_sql_result1))[0])
        boom_car_scale = ((max(temp_sql_result1))[-1])
        conn.commit()

        temp2 = "select * from nor_car"
        cursor.execute(temp2)
        temp_sql_result2 = cursor.fetchall()
        nor_date = ((max(temp_sql_result2))[0])
        nor_car_scale = ((max(temp_sql_result2))[-1])
        conn.commit()

        # boom_car
        temp1 = "select * from boom_car"
        cursor.execute(temp1)
        temp_sql_result = cursor.fetchall()
        # print(("*"*30,temp_sql_result))
        # print(type(temp_sql_result))
        # list_temp_sql_result = []
        for i in temp_sql_result:
            list_temp_sql_result.append(i)

        # nor_car
        temp2 = "select * from nor_car"
        cursor.execute(temp2)
        temp_sql_result2 = cursor.fetchall()
        list_temp_sql_result2 = []
        for i in temp_sql_result2:
            list_temp_sql_result2.append(i)

        list_temp_sql_result = (sorted(list_temp_sql_result, key=self.by_name))
        list_temp_sql_result2 = (sorted(list_temp_sql_result2, key=self.by_name))
        # print(list_temp_sql_result)
        # print(list_temp_sql_result2)
        
        
        # oil_high
        temp5 = "select * from oil_high where id=(select max(id) from oil_high)"

        # cur = conn.cursor(cursorclass=pymysql.cursors.DictCursor)
        cursor.execute(temp5)
        list_temp_sql_result5 = []
        for i in cursor.fetchall():
            for ii in i:
                list_temp_sql_result5.append(ii)
        from math import ceil, floor

        oil_h1_p = (ceil((self.calu_oil_L(float(list_temp_sql_result5[1])))/30.989*100))
        oil_h2_p = (ceil((self.calu_oil_L(float(list_temp_sql_result5[2])))/30.989*100))
        # --->

        # <-- mysql(oil_execl) date --- sum --- sum_ava --- oil_sum_p --- today_p
        temp_oil_execl = "select * from oil_execl"
        cursor.execute(temp_oil_execl)
        temp_sql_result_oil_execl = cursor.fetchall()

        list_temp_sql_result_oil_execl = []
        for i in temp_sql_result_oil_execl:
            list_temp_sql_result_oil_execl.append(i)
        list_temp_sql_result_oil_execl = (sorted(list_temp_sql_result_oil_execl, key=self.by_name))
        # print("*"*10,list_temp_sql_result_oil_execl[-1][0]) #date
        # print("*"*10,list_temp_sql_result_oil_execl[-1][1]) #sum
        # print("*"*10,list_temp_sql_result_oil_execl[-1][2]) #sum_ava
        # print("*"*10,list_temp_sql_result_oil_execl[-1][3]) #oil_sum_p
        # print("*"*10,list_temp_sql_result_oil_execl[-1][-1]) #today_p
        oil_sum = (ceil(float(list_temp_sql_result_oil_execl[-1][1])))
        # print(oil_sum)
        oil_sum_date = list_temp_sql_result_oil_execl[-1][0]
        today_p = list_temp_sql_result_oil_execl[-1][-1]
        oil_sum_p = list_temp_sql_result_oil_execl[-1][3]

                 
        # -->
        # <--查询最近的油量不为0的一条记录
        temp4 = "select * from income_oil"
        cursor.execute(temp4)
        temp_sql_result4 = cursor.fetchall()

        list_temp_sql_result4 = []
        for i in temp_sql_result4:
            list_temp_sql_result4.append(i)
        list_temp_sql_result4 = (sorted(list_temp_sql_result4, key=self.by_name))
        # print(list_temp_sql_result4)

        for i in range(0,-8,-1):
            # 8天以内有无income_oil 不等于0，就更新，等于0就报错，index无法进入，显示income_i没有应用，应该默认为0
            # print(i)
            if (list_temp_sql_result4[i][1]) != str(0):
                # print(i)
                income_i = i
                # print("*"*30,income_i)
                # print((list_temp_sql_result4[income_i]))
                break
            else:
                income_i=0
                # else试用有问题删除 2019.10.4
        # print(list_temp_sql_result4[income_i][0])
        # print(list_temp_sql_result4[income_i][1])
        # -->

        cursor.close()
        conn.close()
        # names = locals()
        '''
        for i in range(11):
            print(i)
            date = (list_temp_sql_result[i][0].strftime("%Y-%m-%d"))
            globals()['date' + str(i)] = ("'%s'"%date)
            globals()['boom' + str(i)] = list_temp_sql_result[i][2]
            globals()['nor' + str(i)] = list_temp_sql_result2[i][2]
            # names['date%s' % i] = list_temp_sql_result[i][0]
            # names['boom%s' % i] = list_temp_sql_result[i][2]
            # names['nor%s' % i] = list_temp_sql_result2[i][2]
        print(list_temp_sql_result[income_i][-1])
        '''
        from math import ceil
        self.render('index.html',oil_sum = oil_sum,today_p = today_p,oil_sum_p = oil_sum_p,oil_sum_date = oil_sum_date,\
            yewei_date = (list_temp_sql_result5[0]),oil_h1_p=(oil_h1_p),oil_h2_p=(oil_h2_p),boom_date = boom_date,boom_car_scale=boom_car_scale,\
            nor_date = nor_date,nor_car_scale=nor_car_scale,\
            income_date = (list_temp_sql_result4[income_i][0]),income_scale=ceil(float(list_temp_sql_result4[income_i][-1])),\
            date0 = ("'%s'"%((list_temp_sql_result[-11][0]).strftime("%Y-%m-%d"))),date1 = ("'%s'"%((list_temp_sql_result[-1][0]).strftime("%Y-%m-%d"))),date2 = ("'%s'"%((list_temp_sql_result[-2][0]).strftime("%Y-%m-%d"))),date3 = ("'%s'"%((list_temp_sql_result[-3][0]).strftime("%Y-%m-%d"))),date4 = ("'%s'"%((list_temp_sql_result[-4][0]).strftime("%Y-%m-%d"))),date5 = ("'%s'"%((list_temp_sql_result[-5][0]).strftime("%Y-%m-%d"))),date6 = ("'%s'"%((list_temp_sql_result[-6][0]).strftime("%Y-%m-%d"))),date7 = ("'%s'"%((list_temp_sql_result[-7][0]).strftime("%Y-%m-%d"))),date8 = ("'%s'"%((list_temp_sql_result[-8][0]).strftime("%Y-%m-%d"))),date9 = ("'%s'"%((list_temp_sql_result[-9][0]).strftime("%Y-%m-%d"))),date10 = ("'%s'"%((list_temp_sql_result[-10][0]).strftime("%Y-%m-%d"))),\
            boom0 = list_temp_sql_result[-11][2],boom1 = list_temp_sql_result[-1][2],boom2 = list_temp_sql_result[-2][2],boom3 = list_temp_sql_result[-3][2],boom4 = list_temp_sql_result[-4][2],boom5 = list_temp_sql_result[-5][2],boom6 = list_temp_sql_result[-6][2],boom7 = list_temp_sql_result[-7][2],boom8 = list_temp_sql_result[-8][2],boom9 = list_temp_sql_result[-9][2],boom10 = list_temp_sql_result[-10][2],\
            nor0 = list_temp_sql_result2[-11][2],  nor1 = list_temp_sql_result2[-1][2],nor2 = list_temp_sql_result2[-2][2],nor3 = list_temp_sql_result2[-3][2],nor4 = list_temp_sql_result2[-4][2],nor5 = list_temp_sql_result2[-5][2],nor6 = list_temp_sql_result2[-6][2],nor7 = list_temp_sql_result2[-7][2],nor8 = list_temp_sql_result2[-8][2],nor9 = list_temp_sql_result2[-9][2],nor10 = list_temp_sql_result2[-10][2],)
        

    def post(self, *args, **kwargs):
        '''
        username = self.get_argument('username',None)
        pwd = self.get_argument('pwd', None)
 
        conn = pymysql.connect(host='127.0.0.1', port=3306, user='root', passwd='root', db='test')
        cursor = conn.cursor()
 
        # %s 要加上'' 否则会出现KeyboardInterrupt的错误
        #temp = "select name from userinfo where name='%s' and password='%s'" % (username, pwd)
        #temp = "INSERT INTO userunfo VALUES('%s','%s')"%(username, pwd)
        #temp = "DELETE FROM userinfo WHERE username='%s'"%(username, pwd)
        temp1 = "select name from userinfo"
        #effect_row = cursor.execute(temp)
        effect_row1 = cursor.execute(temp1)
        result = cursor.fetchall()
        conn.commit()
        cursor.close()
        conn.close()
        for x in result:
            print (x)
        '''
        self.render('poem.html', roads='123')
        '''
        if result:
            self.write('成功')
        else:
            self.write('失败！')
        '''
class dluHandler(tornado.web.RequestHandler):
    boom_car_dict = {}
    nor_car_dict = {}
    income_dict = {}
    def get(self):
        self.render("dlu.html")
    def post(self):
        file_metas = self.request.files["fafafa"]               #获取上传文件信息
        for meta in file_metas:                                 #循环文件信息
            file_name = meta['filename']                        #获取文件的名称
            import os                                           #引入os路径处理模块
            file_name = os.path.join(os.path.dirname(__file__),'static','upload_execl',file_name)
            #os拼接文件保存路径，以字节码模式打开
            with open(file_name,'wb') as up:
                #将文件写入到保存路径目录
                up.write(meta['body'])
        print(file_name)
        # 读取文件
        data = xlrd.open_workbook(file_name)
        # 通过索引顺序获取 ：sheet 0 就是第一个
        table = data.sheets()[0]
        table.col_values(0)
        # 找sum总和
        test_list1=[]
        list_temp =[]

        for i in range(len(table.col_values(10)),3,-1):
            # 1 判断12天内的数据，采用自减也就是从后到前，因为更新是自下而上（先获取总行到3行结束，每次-1）
            # 2 判断总和的话都可以
            # 3 前面3行不要，所以到3结束
                # print(i)
                # print(table.col_values(10)[i-1])
                test_list1.append(table.col_values(10)[i-1])
                if table.col_values(2)[i-1] == ((datetime.date.today()+datetime.timedelta(-12)).strftime("%Y.%m.%d")):
                    list_temp.append(i-1)
                # 判断12天内的第一天在哪行，从那行开始
        # print(min(list_temp))
        ii = min(list_temp)+1
        # print(table.col_values(2)[ii])
        # print(sum(test_list1))
        for x in range(12,0,-1):
            # print(x)
            boom_list = []
            nor_list =[]
            income_list =[]
            curr_date = ((datetime.date.today()+datetime.timedelta(-x+1)).strftime("%Y.%m.%d"))
                #  bug修改20191013 时间不同步最新一天
            while ((ii !=  len(table.col_values(10))) and (table.col_values(2)[ii] == curr_date)):
                    # 将当前日期定义成一个变量，减少反复的复用
                    # curr_date = ((datetime.date.today()+datetime.timedelta(-x)).strftime("%Y.%m.%d"))
                    # 判断语句
                    if table.col_values(3)[ii] == "地面站":
                        boom_list.append((table.col_values(9))[ii])

                        # print(curr_date,":",table.col_values(4)[i],":",(table.col_values(9))[i])
                    elif table.col_values(3)[ii] == "车队":
                        # 向车队list添加当天的所有list
                        nor_list.append((table.col_values(9))[ii])
                        # print(curr_date+":"+table.col_values(4)[i],":",(table.col_values(9))[i])
                    elif table.col_values(3)[ii] == "新疆卓信投资有限公司吉木萨尔县矿区加油站":
                        income_list.append(table.col_values(9)[ii])
                        # print(table.col_values(9)[i])
                    else:
                        # 可写当i和x的时候有未知错误
                        continue
                    ii=ii+1
                    # print(ii)
            # 组成一个完整的dict
            self.boom_car_dict[curr_date] = boom_list
            self.nor_car_dict[curr_date] = nor_list
            self.income_dict[curr_date] = income_list
        # print(sum(test_list1))
            # today_lose_boom = (boom_car_dict[((datetime.date.today()+datetime.timedelta(-1)).strftime("%Y.%m.%d"))])
            # today_lose_nor = (nor_car_dict[((datetime.date.today()+datetime.timedelta(-1)).strftime("%Y.%m.%d"))])
            # from math import ceil
            # today_lose_sum_p = (ceil(sum(today_lose_boom)+sum(today_lose_nor))/sum(test_list1)*100)
            # sum_p = (ceil((test_list1)/(30.989*2*100)))
        # 
        date1 = ((datetime.date.today()+datetime.timedelta(-1)).strftime("%Y.%m.%d"))
        today_lose_boom = (self.boom_car_dict[((datetime.date.today()+datetime.timedelta(-1)).strftime("%Y.%m.%d"))])
        today_lose_nor = (self.nor_car_dict[((datetime.date.today()+datetime.timedelta(-1)).strftime("%Y.%m.%d"))])
        from math import ceil
        today_lose_sum_p = (ceil((((sum(today_lose_boom))+(sum(today_lose_nor)))/(30989*2))*100))
        sum_p = (ceil((sum(test_list1))/(30989*2)*100))

        conn = pymysql.connect(host='127.0.0.1', port=3306, user='root', passwd='root', db='test')
        cursor = conn.cursor()
        temp = "INSERT IGNORE INTO oil_execl (date,sum,sum_ava,oil_sum_p,today_percent) VALUES('%s','%s','%s','%s','%s')"%(date1,sum(test_list1),sum(test_list1)-7000,sum_p,today_lose_sum_p)
        cursor.execute(temp)
        # print("269")
        cursor.fetchall()

        conn.commit()
        cursor.close()
        conn.close()

        for i in self.nor_car_dict:
            conn = pymysql.connect(host='127.0.0.1', port=3306, user='root', passwd='root', db='test')
            cursor = conn.cursor()
            temp = "INSERT IGNORE INTO nor_car (data,oil_scale) VALUES('%s','%s')"%(i,sum(self.nor_car_dict[i]))
            cursor.execute(temp)
            # print("282")
            # cursor.execute(temp1)
            cursor.fetchall()
            # print((max(temp_sql_result))[0])
            # print((max(temp_sql_result))[-1])
            conn.commit()
            cursor.close()
            conn.close()

        for i in self.boom_car_dict:
            conn = pymysql.connect(host='127.0.0.1', port=3306, user='root', passwd='root', db='test')
            cursor = conn.cursor()
            temp = "INSERT IGNORE INTO boom_car (data,oil_scale) VALUES('%s','%s')"%(i,sum(self.boom_car_dict[i]))
            cursor.execute(temp)
            # print("296")
            # temp_sql_result = cursor.fetchall()

            conn.commit()
            cursor.close()
            conn.close()

        for i in self.income_dict:
            conn = pymysql.connect(host='127.0.0.1', port=3306, user='root', passwd='root', db='test')
            cursor = conn.cursor()
            temp = "INSERT IGNORE INTO income_oil (data,oil_scale) VALUES('%s','%s')"%(i,sum(self.income_dict[i]))
            # print("307")
            cursor.execute(temp)
            cursor.fetchall()
            conn.commit()
            cursor.close()
            conn.close()
        # time.sleep(3)
        # self.redirect("/alertindex.html")
        self.render("alertindex.html")

    '''
    def by_shengxu(self,t):
        # 配合sort升序list的函数
        return(t[0])
    '''

class CalcuHandler(tornado.web.RequestHandler):
    def by_shengxu(self,t):
        return(t[0])
    def calu_oil_L(self,oil_H):
        import math
        oil_R = 1.2
        oil_L = 6.05
        oil_C = 0.6
        L = (oil_L*((math.pi * oil_R**2)/2-(oil_R-oil_H)*math.sqrt(2*oil_H*oil_R-oil_H**2)-oil_R**2*math.asin(1-oil_H/oil_R))+(math.pi*oil_C)/(3*oil_R)*(3*oil_R**2*oil_H-oil_R**3+(oil_R-oil_H)**3))
        return L
    def get(self):
        # 两个文本框 一个提交按钮 ，post提交后转入tornado post方法
        '''
        conn = pymysql.connect(host='127.0.0.1', port=3306, user='root', passwd='root', db='test')
        cursor = conn.cursor()
        temp1 = "select * from oil_high where id=(select max(id) from oil_high)"
        cursor.execute(temp1)
        temp_sql_result1 = cursor.fetchall()
        list_temp_sql_result5 = []
        for i in temp_sql_result1:
            list_temp_sql_result5.append(i)
        list_temp_sql_result5 = (sorted(list_temp_sql_result5, key=self.by_shengxu))
        print((max(temp_sql_result1))[0])
        print((max(temp_sql_result1))[-1])
        # oil_date = (((temp_sql_result1))[0])
        # boom_car_scale = (((temp_sql_result1))[-1])
        conn.commit()
        cursor.close()
        conn.close()
        # self.render('yewei1.html', oil_h1=oil_h1,oil_h2=oil_h2,oil_sum=oil_sum,oil_sum_ava=oil_sum_ava,oil_date=date1)
        '''

        self.render('yewei1.html',oil_h1=0,oil_h2=0,oil_sum=0,oil_sum_ava=0,oil_date=0)

    def post(self):
        oil_h1 = float(self.get_argument('oil_h1', '1'))
        oil_h2 = float(self.get_argument('oil_h2', '1'))
        oil_sum = self.calu_oil_L(oil_h1)+self.calu_oil_L(oil_h2)
        oil_sum_ava = oil_sum-0.7
        '''
        # 总容量30.989
        oil_h1_p = oil_h1/30.989
        oil_h2_p = oil_h2/30.989
        oil_sum_p = oil_sum/(30.989*2)
        '''
        # 
        import time
        # date = time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())
        date1 = time.strftime("%Y-%m-%d", time.localtime())
        # 插数据
        conn = pymysql.connect(host='127.0.0.1', port=3306, user='root', passwd='root', db='test')
        cursor = conn.cursor()
        temp1 = "INSERT INTO oil_high (date,oil_h1,oil_h2,oil_sum,oil_sum_ava) VALUES('%s','%s','%s','%s','%s')"%(date1,oil_h1,oil_h2,oil_sum,oil_sum_ava)
        cursor.execute(temp1)
        temp_sql_result1 = cursor.fetchall()
        # boom_date = (((temp_sql_result1))[0])
        # boom_car_scale = (((temp_sql_result1))[-1])
        conn.commit()
        cursor.close()
        conn.close()

        self.render('yewei1.html', oil_h1=oil_h1,oil_h2=oil_h2,oil_sum=oil_sum,oil_sum_ava=oil_sum_ava,oil_date=date1)

class Android_json_Handler(tornado.web.RequestHandler):

    def get(self):
        conn = pymysql.connect(host='127.0.0.1', port=3306, user='root', passwd='root', db='test')
        cursor = conn.cursor()

        temp1 = "select name from zjd_name"
        cursor.execute(temp1)
        temp_sql_result1 = cursor.fetchall()
        list_temp_sql_result123 = []
        for i in range(len(temp_sql_result1)):
            list_temp_sql_result123.append(temp_sql_result1[i][0])
            # print(temp_sql_result1[i][0])
            # print(i)
        content = (",".join(list_temp_sql_result123))
        json_list={}
        json_list['result'] = content
        # ---
        temp1 = "select name from zjd_type_name"
        cursor.execute(temp1)
        temp_sql_result1 = cursor.fetchall()
        list_temp_sql_result123 = []
        for i in range(len(temp_sql_result1)):
            list_temp_sql_result123.append(temp_sql_result1[i][0])
            # print(temp_sql_result1[i][0])
            # print(i)
        content = (",".join(list_temp_sql_result123))
        json_list['zjd_type_result'] = content
        # ---
        json_data = json.dumps(json_list, ensure_ascii=False)
        print(json_list)
        conn.commit()
        cursor.close()
        conn.close()

        self.write(json_data)
        # self.render("test1.html",test_j = json_data)
    def post(self):
        self.render("dlu.html")

class ImgHandler(tornado.web.RequestHandler):

    def get(self):
        self.write('<html><body><form enctype="multipart/form-data" action="/upload" method="post" name="up_load">'
                   '<input type="file" name="files">'
                   '<input type="submit" value="Submit">'
                   '</form></body></html>')

class Upload(tornado.web.RequestHandler):

  def post(self):
    # 这部分就是上传的文件,想要查看更多可以print self.request看看
    # 该文件返回一个元素为字典的列表
    imgfile = self.request.files["files"][0]
    # print(imgfile)
    file_name = imgfile['filename']
    print(file_name)
    # for img in imgfile:
    #   file_name = img['filename']
      # img有三个键值对可以通过img.keys()查看
      # 分别是 'filename', 'body', 'content_type' 很明显对应着文件名,内容(二进制)和文件类型
    import os
    file_name = os.path.join(os.path.dirname(__file__),'static','upload_img',imgfile['filename'])
            #os拼接文件保存路径，以字节码模式打开
    with open(file_name,'wb') as f:
        # 文件内容保存 到'/static/uploads/{{filename}}'
        f.write(imgfile['body'])
class Up_zjdHandler(tornado.web.RequestHandler):
  def get(self):
      drill = self.get_argument("drill","null")
      print(drill)
      if drill=='6号牙轮钻机':
          content = "6号牙轮钻机"
          print("6号牙轮钻机")
      elif drill=='1号牙轮钻机':
        content = "1"
        print("1")
      else:
          content = "else"
          print('else')
      json_list={}
      json_list['zjd_drill_sum'] = content
      json_list['zjd_drill_cishu'] = '9'

      json_data = json.dumps(json_list, ensure_ascii=False)
      print(json_list)

      self.write(json_data)

  def post(self):
        m_people = self.get_argument("m_people","null")
        s_people = self.get_argument("s_people","null")
        many_people = self.get_argument("many_people","null")
        classes = self.get_argument("classes","null")
        workplace = self.get_argument("workplace","null")
        drill_type = self.get_argument("drill_type","null")
        Accumulated_scale = self.get_argument("Accumulated_scale","null")
        Accumulated_frequency = self.get_argument("Accumulated_frequency","null")
        drill_id = self.get_argument("drill_id","null")
        scale = self.get_argument("scale","null")
        frequency = self.get_argument("frequency","null")
        holetext = self.get_argument("holetext","null")
        oil = self.get_argument("oil","null")
        oil2 = self.get_argument("oil2","null")

        print('*'*30)
        print(m_people)
        print(s_people)
        print(many_people)
        print(classes)
        print(workplace)
        print(drill_type)
        print(Accumulated_scale)
        print(Accumulated_frequency)
        print(drill_id)
        print(scale)
        print(frequency)
        print(holetext)
        print(oil)
        print(oil2)

        print('*'*30)
        #   初始化数据库

        # if先查找数据库是否有一样数据，有则直接返回false
        # else没有，则Insert数据(date默认2019-11-01;status默认为0),返回ok
        conn = pymysql.connect(host='127.0.0.1', port=3306, user='root', passwd='root', db='test')
        cursor = conn.cursor()

        import time
        # date = time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())
        date = time.strftime("%Y-%m-%d", time.localtime())

        temp1 = "select * from zjd_main where drill_detailed='%s' AND date='%s'"%(holetext,date)
        cursor.execute(temp1)
        temp_sql_result1 = cursor.fetchall()
        if temp_sql_result1:
                json_list={}
                json_list['result'] = 'exists'
        else:
            temp1 = "INSERT INTO zjd_main(date,classes,zjd_type,main_worker,assist_worker,other_worker,hole_sum,hole_count,drill_id,drill_detailed,oil_main,oil_other,status,workplace) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')"%(date,classes,drill_type,m_people,s_people,many_people,frequency,scale,drill_id,holetext,oil,oil2,0,workplace)
            # temp1 = "select name from zjd_name"
            cursor.execute(temp1)
            temp1 = "select * from zjd_main where drill_detailed='%s' AND date='%s'"%(holetext,date)
            cursor.execute(temp1)
            temp_sql_result1 = cursor.fetchall()
            if temp_sql_result1:
                print("success!")

                json_list={}
                json_list['result'] = 'ok'
            else:
                json_list={}
                json_list['result'] = 'okok'

        json_data = json.dumps(json_list, ensure_ascii=False)

        self.write(json_data)

        conn.commit()
        cursor.close()
        conn.close()

class zjd_Site_Table_SelectHandler(tornado.web.RequestHandler):
  def get(self):
      self.write("zjd_Site_Table_SelectHandler get")

  def post(self):
    #   接受参数
    table_people = self.get_argument("table_people","null")

    print("*"*30,table_people)
    """
    # 检查输入参数
    # 为空   则查询全部
    # 不为空 则模糊查找 --有结果显示 没结果返回前端"没有匹配到字段"
    # 加分页
    """

    #   数据库查找id
        #初始化数据库
    conn = pymysql.connect(host='127.0.0.1', port=3306, user='root', passwd='root', db='test')
    cursor = conn.cursor()
        #查找返回校验结果
    # temp1 = select * from zjd_main where main_worker LIKE '%一%' OR assist_worker LIKE '%一%' OR other_worker LIKE '%三%'
    temp1 = ("select * from zjd_main where main_worker LIKE '%%%s%%' OR assist_worker LIKE '%%%s%%' OR other_worker LIKE '%%%s%%'" %(table_people,table_people,table_people))
    print("*"*30,temp1)
    cursor.execute(temp1)
    temp_sql_result1 = cursor.fetchall()
    #   有的话，修改
    if bool(temp_sql_result1):
        # print("true")
        json_list={}
        json_main={}
        list_temp_sql_result123=[]
        for i in range(len(temp_sql_result1)):
            json_main={}
            json_main["id"] = temp_sql_result1[i][0]
            json_main["date"] = str(temp_sql_result1[i][1])
            json_main["classes"] = temp_sql_result1[i][2]
            json_main["drill_type"] = temp_sql_result1[i][3]
            json_main["m_p"] = temp_sql_result1[i][4]
            json_main["s_p"] = temp_sql_result1[i][5]
            json_main["many_p"] = temp_sql_result1[i][6]
            json_main["drill_sum"] = temp_sql_result1[i][7]
            json_main["drill_count"] = temp_sql_result1[i][8]
            json_main["drill_id"] = temp_sql_result1[i][9]
            json_main["drill_detail"] = temp_sql_result1[i][10]
            json_main["drill_oil"] = temp_sql_result1[i][11]
            json_main["drill_oil2"] = temp_sql_result1[i][12]

            list_temp_sql_result123.append(json_main)
            # print(temp_sql_result1[i][0])
            # print(i)
        # print(list_temp_sql_result123)
        # content = (",".join(list_temp_sql_result123))
        json_list={}
        json_list['result'] = list_temp_sql_result123
        json_list['status'] = 'true'

        json_data = json.dumps(json_list, ensure_ascii=False)
        # print(json_data)
        self.write(json_data)
    #   没有的话,添加
    else:
        # print("false")
        json_list={}
        json_list['status'] = 'false'
        json_data = json.dumps(json_list, ensure_ascii=False)
        self.write(json_data)

    # ----
    # json_list={}
    # json_list['result'] = "ok"
    # json_data = json.dumps(json_list, ensure_ascii=False)
    # self.write(json_data)

    # 
    conn.commit()
    cursor.close()
    conn.close()
class zjd_Site_Table_UpdateHandler(tornado.web.RequestHandler):
  def get(self):
      self.write("zjd_Site_Table_UpdateHandler get")

  def post(self):
    #   接受参数
    table_id = self.get_argument("table_id","null")
    # print("*"*60+table_id+'*')
    table_date = self.get_argument("table_date","null")
    classes = self.get_argument("classes","null")
    drill_type = self.get_argument("drill_type","null")
    m_people = self.get_argument("m_people","null")
    s_people = self.get_argument("s_people","null")
    many_people = self.get_argument("many_people","null")

    frequency = self.get_argument("frequency","null")
    scale = self.get_argument("scale","null")

    drill_id = self.get_argument("drill_id","null")#钻头id
    holetext = self.get_argument("holetext","null")
    oil = self.get_argument("oil","null")
    oil2 = self.get_argument("oil2","null")
    workplace = self.get_argument("workplace","null")

    # Accumulated_scale = self.get_argument("Accumulated_scale","null")#钻头累计长度
    # Accumulated_frequency = self.get_argument("Accumulated_frequency","null")#钻头累计次数
    # drill_id = self.get_argument("drill_id","null")#钻头id

        #   数据库查找id
            #初始化数据库
    conn = pymysql.connect(host='127.0.0.1', port=3306, user='root', passwd='root', db='test')
    cursor = conn.cursor()

    if table_id == "":
        # print("None")

        temp1 = ("INSERT INTO zjd_main (date,classes,zjd_type,workplace,main_worker,assist_worker,other_worker,hole_sum,hole_count,drill_id,drill_detailed,oil_main,oil_other) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')" 
        %(table_date,classes,drill_type,workplace,m_people,s_people,many_people,frequency,scale,drill_id,holetext,oil,oil2))
        # print(temp1)
        cursor.execute(temp1)
        temp_sql_result1 = cursor.fetchall()
        
        json_list={}
        json_list['result'] = "ok"
        json_data = json.dumps(json_list, ensure_ascii=False)
        # self.write(json_data)
    else:


            #查找返回校验结果
        temp1 = ("select * from zjd_main where id=%s" %table_id)
        cursor.execute(temp1)
        temp_sql_result1 = cursor.fetchall()
        #   有的话，修改
        if bool(temp_sql_result1):
            temp1 = ("UPDATE zjd_main SET date=%s,classes='%s',zjd_type='%s',workplace='%s',main_worker='%s',assist_worker='%s',other_worker='%s',hole_sum='%s',hole_count='%s',drill_id='%s',drill_detailed='%s',oil_main='%s',oil_other='%s' where id=%s" 
            %(table_date,classes,drill_type,workplace,m_people,s_people,many_people,frequency,scale,drill_id,holetext,oil,oil2,table_id))
            print(temp1)
            cursor.execute(temp1)
            temp_sql_result1 = cursor.fetchall()
            print("true")
            json_list={}
            json_list['result'] = "ok"

        #   没有的话,添加
        else:

            json_list={}
            json_list['result'] = "false"

    json_data = json.dumps(json_list, ensure_ascii=False)
    
    self.write(json_data)

        # 
    conn.commit()
    cursor.close()
    conn.close()
class zjd_Site_Table_DeleteHandler(tornado.web.RequestHandler):
  def get(self):
      self.write("zjd_Site_Table_DeleteHandler get")
  def post(self):
    #   接受参数
    table_id = self.get_argument("table_id","null")

    """
    检查id
    根据id软删除 修改statu为9
    
    """
    #   数据库查找id
        #初始化数据库
    conn = pymysql.connect(host='127.0.0.1', port=3306, user='root', passwd='root', db='test')
    cursor = conn.cursor()
        #查找返回校验结果
    temp1 = ("UPDATE zjd_main SET `status`='9' where id=%s" %table_id)
    cursor.execute(temp1)
    temp_sql_result1 = cursor.fetchall()

    temp1 = ("select * from zjd_main where status = '9' AND id=%s" %table_id)
    cursor.execute(temp1)
    temp_sql_result1 = cursor.fetchall()
    #   有的话，修改
    if bool(temp_sql_result1):
        json_list={}
        json_list['result'] = "ok"
        print("true")

    #   没有的话,添加
    else:
        print("false")
    # ----
        json_list={}
        json_list['result'] = "false"
    # print(json_list)
    # ---
    json_data = json.dumps(json_list, ensure_ascii=False)
    
    self.write(json_data)

    # 
    conn.commit()
    cursor.close()
    conn.close()

class zjd_SelectDrillHandler(tornado.web.RequestHandler):
  def get(self):
    self.write("zjd_SelectDrillHandler get")

  def post(self):
      self.write("zjd_AddDrillHandler_post")
class zjd_AddDrillHandler(tornado.web.RequestHandler):
  def get(self):
        drill = self.get_argument("drill","null")
        if (drill=='1号牙轮钻机'):
            table = 'drill_1'
            num=1
        elif (drill=='2号牙轮钻机'):
            table = 'drill_2'
            num=2
        elif (drill=='6号牙轮钻机'):
            table = 'drill_6'
            num=6
        elif (drill=='7号牙轮钻机'):
            table = 'drill_7'
            num=7
        elif (drill=='8号牙轮钻机'):
            table = 'drill_8'
            num=8
        elif (drill=='9号牙轮钻机'):
            table = 'drill_9'
            num=9

        conn = pymysql.connect(host='127.0.0.1', port=3306, user='root', passwd='root', db='test')
        cursor = conn.cursor()

        temp1 = ("select * from %s where drill_status = 1" %table)

        cursor.execute(temp1)
        temp_sql_result1 = cursor.fetchall()
        json_list={}
        if len(temp_sql_result1) == 0:
            import time
            os_time = time.strftime("%Y%m%d%H%M%S", time.localtime())
            json_list['drillname'] = str(num)+'-'+str(os_time)
        else:
            # ---drill_name
            json_list['drillname'] = 'false'

        json_data = json.dumps(json_list, ensure_ascii=False)
        conn.commit()
        cursor.close()
        conn.close()
        self.write(json_data)
        print("ok")

  def post(self):
      self.write("zjd_AddDrillHandler_post")
class zjd_DeleteDrillHandler(tornado.web.RequestHandler):
  def get(self):
        drill_name = self.get_argument("drill_name","null")
        drill = self.get_argument("drill","null")
        if (drill=='1号牙轮钻机'):
            table = 'drill_1'
        elif (drill=='2号牙轮钻机'):
            table = 'drill_2'
        elif (drill=='6号牙轮钻机'):
            table = 'drill_6'
        elif (drill=='7号牙轮钻机'):
            table = 'drill_7'
        elif (drill=='8号牙轮钻机'):
            table = 'drill_8'
        elif (drill=='9号牙轮钻机'):
            table = 'drill_9'

        conn = pymysql.connect(host='127.0.0.1', port=3306, user='root', passwd='root', db='test')
        cursor = conn.cursor()
        temp1 = ("UPDATE %s SET drill_status=2 where drill_name='%s'" %(table,drill_name))
        cursor.execute(temp1)
        cursor.fetchall()
        # ---检查是否成功
        temp2 = ("select * from %s where time = (SELECT max(time) from %s) AND drill_status = 1" %(table,table))
        cursor.execute(temp2)
        temp_sql_result2 = cursor.fetchall()
        json_list={}
        if len(temp_sql_result2) == 0:
            json_list['result']='ok'
        else:
            json_list['result']='false'

        json_data = json.dumps(json_list, ensure_ascii=False)

        self.write(json_data)
        conn.commit()
        cursor.close()
        conn.close()

  def post(self):
      self.write("zjd_AddDrillHandler_post")
class zjd_testHandler(tornado.web.RequestHandler):
  def get(self):
    self.render("drill_main.html")
class zjd_main_json_Handler(tornado.web.RequestHandler):

    def get(self):
        # table_id = self.get_argument("table_id","")


        # print("*"*30,table_id)

        conn = pymysql.connect(host='127.0.0.1', port=3306, user='root', passwd='root', db='test')
        cursor = conn.cursor()
        """
        查询全部字段,且status为0的数据 待修改
        """
        # select * from zjd_main where STATUS = '0' AND main_worker = '张六' OR assist_worker = '张六' OR other_worker='张六'
        temp1 = "select * from zjd_main where STATUS = '0'"
        # else:
        #     temp1 = "select * from zjd_main where STATUS = '0' AND main_worker = '%s' OR assist_worker = '%s' OR other_worker='%s'"%(table_id,table_id,table_id)
        cursor.execute(temp1)
        temp_sql_result1 = cursor.fetchall()
        # print(temp_sql_result1)
        list_temp_sql_result123 = []
        json_main={}
        for i in range(len(temp_sql_result1)):
            json_main={}
            json_main["id"] = temp_sql_result1[i][0]
            json_main["date"] = str(temp_sql_result1[i][1])
            json_main["classes"] = temp_sql_result1[i][2]
            json_main["drill_type"] = temp_sql_result1[i][3]
            json_main["m_p"] = temp_sql_result1[i][4]
            json_main["s_p"] = temp_sql_result1[i][5]
            json_main["many_p"] = temp_sql_result1[i][6]
            json_main["drill_sum"] = temp_sql_result1[i][7]
            json_main["drill_count"] = temp_sql_result1[i][8]
            json_main["drill_id"] = temp_sql_result1[i][9]
            json_main["drill_detail"] = temp_sql_result1[i][10]
            json_main["drill_oil"] = temp_sql_result1[i][11]
            json_main["drill_oil2"] = temp_sql_result1[i][12]
            json_main["workplace"] = temp_sql_result1[i][14]

            list_temp_sql_result123.append(json_main)
            # print(temp_sql_result1[i][0])
            # print(i)
        # print(list_temp_sql_result123)
        # content = (",".join(list_temp_sql_result123))
        json_list={}
        json_list['result'] = list_temp_sql_result123
        # print(json_list)
        # ---
        json_data = json.dumps(json_list, ensure_ascii=False)
        print(json_list)
        conn.commit()
        cursor.close()
        conn.close()

        self.write(json_data)
        # self.render("drill_main.html")
        # self.render("test1.html",test_j = json_data)
    def post(self):
        # self.render("dlu.html")
        pass
class zjd_worker_json_Handler(tornado.web.RequestHandler):

    def get(self):
        table_id = self.get_argument("table_id","")


        # print("*"*30,table_id)

        conn = pymysql.connect(host='127.0.0.1', port=3306, user='root', passwd='root', db='test')
        cursor = conn.cursor()
        """
        查询全部字段,且status为0的数据 待修改
        """
        # select * from zjd_main where STATUS = '0' AND main_worker = '张六' OR assist_worker = '张六' OR other_worker='张六'
        # temp1 = "select * from zjd_main where STATUS = '0'"
        temp1 = "select * from zjd_main where (STATUS = '0') AND (main_worker = '%s' OR assist_worker = '%s' OR other_worker LIKE '%%%s%%')"%(table_id,table_id,table_id)
        print(temp1)
        cursor.execute(temp1)
        temp_sql_result1 = cursor.fetchall()
        # print(temp_sql_result1)
        list_temp_sql_result123 = []
        json_main={}
        for i in range(len(temp_sql_result1)):
            json_main={}
            json_main["id"] = temp_sql_result1[i][0]
            json_main["date"] = str(temp_sql_result1[i][1])
            json_main["classes"] = temp_sql_result1[i][2]
            json_main["drill_type"] = temp_sql_result1[i][3]
            json_main["m_p"] = temp_sql_result1[i][4]
            json_main["s_p"] = temp_sql_result1[i][5]
            json_main["many_p"] = temp_sql_result1[i][6]
            json_main["drill_sum"] = temp_sql_result1[i][7]
            json_main["drill_count"] = temp_sql_result1[i][8]
            json_main["drill_id"] = temp_sql_result1[i][9]
            json_main["drill_detail"] = temp_sql_result1[i][10]
            json_main["drill_oil"] = temp_sql_result1[i][11]
            json_main["drill_oil2"] = temp_sql_result1[i][12]
            json_main["workplace"] = temp_sql_result1[i][14]

            list_temp_sql_result123.append(json_main)
            # print(temp_sql_result1[i][0])
            # print(i)
        # print(list_temp_sql_result123)
        # content = (",".join(list_temp_sql_result123))
        json_list={}
        json_list['result'] = list_temp_sql_result123
        # print(json_list)
        # ---
        json_data = json.dumps(json_list, ensure_ascii=False)
        print(json_list)
        conn.commit()
        cursor.close()
        conn.close()

        self.write(json_data)
        # self.render("drill_main.html")
        # self.render("test1.html",test_j = json_data)
    def post(self):
        # self.render("dlu.html")
        pass
class zjd_worker_Handler(tornado.web.RequestHandler):
  def get(self):
    table_id = self.get_argument("table_id","")
    self.render("drill_worker.html",table_id=table_id)
settings = {
    'template_path':os.path.join(os.path.dirname(__file__),"template"),
    'static_path':os.path.join(os.path.dirname(__file__),"static"),
#是/不是\,不然会报错OSError: [Errno 22] Invalid argument
}
current_path = os.path.dirname(__file__)
application = tornado.web.Application([
    (r"/index", SelectHandler),
    (r"/execlupload", dluHandler),
    (r"/dlu", dluHandler),
    (r"/yewei", CalcuHandler),
    (r"/test1", Android_json_Handler),
    (r"/img", ImgHandler),
    (r"/upload", Upload),
    (r"/up_zjd", Up_zjdHandler),
    (r"/zjd_drill_select", zjd_SelectDrillHandler),
    (r"/zjd_drill_add", zjd_AddDrillHandler),
    (r"/zjd_drill_del", zjd_DeleteDrillHandler),
    (r"/zjd_main", zjd_testHandler),
    (r"/zjd_main_json", zjd_main_json_Handler),
    (r"/zjd_table_select", zjd_Site_Table_SelectHandler),
    (r"/zjd_table_update", zjd_Site_Table_UpdateHandler),
    (r"/zjd_table_delete", zjd_Site_Table_DeleteHandler),
    (r"/zjd_worker_json", zjd_worker_json_Handler),
    (r"/zjd_worker", zjd_worker_Handler),

],**settings)


if __name__ == "__main__":
    application.listen(8000)
    tornado.ioloop.IOLoop.instance().start()