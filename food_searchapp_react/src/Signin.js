import React from 'react'

function Signin() {
  return (
    <div>
        <form>
            <div>
                <input type='text' placeholder='ユーザーID' />
            </div>

            <div>
                <input type='password' placeholder='パスワード' />
            </div>

            <div>
                <input type='submit' value="ログイン"/>
            </div>
        </form>

        <a href='/'>パスワードをお忘れの方</a>

        <div>
            <button>新規登録</button>
        </div>
    </div>
  )
}

export default Signin